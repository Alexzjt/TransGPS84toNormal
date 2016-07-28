import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class ChangeTollData {
	public static void main(String[] args){
		try {
			BufferedReader reader =new BufferedReader(new FileReader("E:\\zjt\\云南收费数据\\530000.data"));
			BufferedWriter writer=new BufferedWriter(new FileWriter("E:\\zjt\\云南收费数据\\5300001.data"));
			String line;
			while((line=reader.readLine())!=null){
				String[] array=line.split(",");
				String lon=array[9].substring(0,3)+"."+array[9].substring(3);
				String lat=array[10].substring(0,2)+"."+array[9].substring(2);
				writer.write(line.replace(array[9], lon).replace(array[10], lat)+"\r\n");
			}
			reader.close();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
