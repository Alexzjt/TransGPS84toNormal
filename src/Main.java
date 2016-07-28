
public class Main {
	public static void main(String[] args){
		Gps test=PositionUtil.gps84_To_Gcj02(99.067416,25.002150);
		System.out.println(test.toString());
	}
}
