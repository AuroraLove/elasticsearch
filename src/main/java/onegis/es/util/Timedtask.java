package onegis.es.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import onegis.es.service.EsService;
import onegis.es.service.GetData;
import onegis.psde.psdm.SObject;
import onegis.result.response.ResponseResult;
import org.dtools.ini.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @ProjectName onegis.model.web
 * @Author Z.CR
 * @Date 2018/12/5 9:37
 * @Description:定时任务
 * @Version 1.0
 */
@Component
public class Timedtask {

	@Autowired
	private EsService esService;

	@Autowired
	private GetData template;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Scheduled(cron = "${model.Btime.cron}")
	public  void reportCurrentTime() throws Exception {
        System.out.println("系统时间："+dateFormat.format(new Date()));
		readIni();
		if(true){
			writeIni();
		}
	}
	public  void readIni() throws Exception{
		read("readIni");
	}

	public  void writeIni() throws Exception {
		read("read");
	}

	public  void read(String flag) throws Exception{
		// 读取本次数据拉取的起始时间，再根据时间间隔计算出结束时间，构成一次时间段，拿到时间段去请求数据
		IniFile iniFile = new BasicIniFile();
        File file = ResourceUtils.getFile("classpath:time.ini");
        System.out.println("配置文件："+file);
		IniFileReader reader = new IniFileReader(iniFile, file);
		reader.read();

		IniSection iniSection = iniFile.getSection("StartTime");
		IniItem iniItem = iniSection.getItem("Stime");
		String sTime = iniItem.getValue();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


		Long time = simpleDateFormat.parse(sTime).getTime();
		IniSection iniS = iniFile.getSection("BetweenTime");
		IniItem iniI = iniS.getItem("Btime");
		Long btime = Long.valueOf(iniI.getValue())*1000;
		Long time3 = time + btime;
		Date date = new Date(time3);
		String Etime = simpleDateFormat.format(date);


		iniItem.setValue(Etime);
		iniSection.addItem(iniItem);
		iniFile.addSection(iniSection);
		if (flag.equals("read")){
			// 任务结束后，将最新的时间记录到ini文件里 ==》 startTime = endTime
			IniFileWriter writer = new IniFileWriter(iniFile, file);
			writer.write();
		}
		if (flag.equals("readIni")){
			save(sTime,Etime);
		}

	}

	public void save(String sTime,String eTime) throws Exception{

		/**
		 * 总记录数
		 */
		Long count = 0L;
		/**
		 * 总页数
		 */
		int total = 0;
		/**
		 * 显示第几页
		 */
		int pageNum = 1;
		/**
		 * 一页显示多少
		 */
		int pageSize= 500;
		/**
		 * 总页数
		 */
		int pages = 0;
		/**
		 * 最后一页的大小
		 */
		int lastCount =0;

		ResponseResult result = template.getSObject1(sTime, eTime,pageNum,pageSize);
		LinkedHashMap map = (LinkedHashMap) result.getData();

		if(map.get("total")!=null){
			// 总记录数
			total = (Integer) map.get("total");
			pages = total/pageSize;
			lastCount = total%pageSize;
			System.out.println("============总页数:" + pages +" =========================");
            System.out.println("数据拉取开始时间：" + sTime);
            System.out.println("数据拉取结束时间：" + eTime);
        }

		for(int i = 0;i<pages;i++){
			ResponseResult resultSub = template.getSObject1(sTime, eTime,pageNum,pageSize);
			LinkedHashMap mapSub = (LinkedHashMap) resultSub.getData();
			List<SObject> sObjects = new ObjectMapper().convertValue(mapSub.get("list"), new TypeReference<List<SObject>>() { });
			esService.saveSObject(sObjects);
			pageNum++;
			count += sObjects.size();
			System.out.println("第：" + i + " 页存储成功");
			System.out.println("以存储：" + count + " 条数据");
		}

		if(lastCount != 0){
			ResponseResult resultSub = template.getSObject1(sTime, eTime,pageNum,lastCount);
			LinkedHashMap mapSub = (LinkedHashMap) resultSub.getData();
			List<SObject> sObjects = new ObjectMapper().convertValue(mapSub.get("list"), new TypeReference<List<SObject>>() { });
			esService.saveSObject(sObjects);
			count += sObjects.size();
			System.out.println("最后一页存储成功");
			System.out.println("以存储：" + count + " 条数据");
		}
	}

}
