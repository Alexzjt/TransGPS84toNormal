import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;



public final class TransGPS84toNormal {
	private static double casm_rr;
	private static long casm_t1;
	private static long casm_t2;
	private static double casm_x1;
	private static double casm_y1;
	private static double casm_x2;
	private static double casm_y2;
	private static double casm_f;

	private static double yj_sin2(double x) {
		double tt;
		double ss;
		int ff;
		double s2;
		int cc;

		ff = 0;
		if (x < 0) {
			x = -x;
			ff = 1;
		}
		cc = (int) (x / 6.28318530717959);
		tt = x - cc * 6.28318530717959;
		if (tt > 3.1415926535897932) {
			tt = tt - 3.1415926535897932;
			if (ff == 1)
				ff = 0;
			else if (ff == 0)
				ff = 1;
		}
		x = tt;
		ss = x;
		s2 = x;
		tt = tt * tt;
		s2 = s2 * tt;
		ss = ss - s2 * 0.166666666666667;
		s2 = s2 * tt;
		ss = ss + s2 * 8.33333333333333E-03;
		s2 = s2 * tt;
		ss = ss - s2 * 1.98412698412698E-04;
		s2 = s2 * tt;
		ss = ss + s2 * 2.75573192239859E-06;
		s2 = s2 * tt;
		ss = ss - s2 * 2.50521083854417E-08;
		if (ff == 1)
			ss = -ss;

		return ss;
	}

	private static double Transform_yj5(double x, double y) {
		double tt;

		tt = 300 + 1 * x + 2 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.sqrt(x * x));
		tt = tt + (20 * yj_sin2(18.849555921538764 * x) + 20 * yj_sin2(6.283185307179588 * x)) * 0.6667;
		tt = tt + (20 * yj_sin2(3.141592653589794 * x) + 40 * yj_sin2(1.047197551196598 * x)) * 0.6667;
		tt = tt + (150 * yj_sin2(0.2617993877991495 * x) + 300 * yj_sin2(0.1047197551196598 * x)) * 0.6667;

		return tt;
	}

	private static double Transform_yjy5(double x, double y) {
		double tt;

		tt = -100 + 2 * x + 3 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.sqrt(x * x));
		tt = tt + (20 * yj_sin2(18.849555921538764 * x) + 20 * yj_sin2(6.283185307179588 * x)) * 0.6667;
		tt = tt + (20 * yj_sin2(3.141592653589794 * y) + 40 * yj_sin2(1.047197551196598 * y)) * 0.6667;
		tt = tt + (160 * yj_sin2(0.2617993877991495 * y) + 320 * yj_sin2(0.1047197551196598 * y)) * 0.6667;

		return tt;
	}

	private static double Transform_jy5(double x, double xx) {
		double n;
		double a;
		double e;

		a = 6378245;
		e = 0.00669342;
		n = Math.sqrt(1 - e * yj_sin2(x * 0.0174532925199433) * yj_sin2(x * 0.0174532925199433));
		n = (xx * 180) / (a / n * Math.cos(x * 0.0174532925199433) * 3.1415926);

		return n;
	}

	private static double Transform_jyj5(double x, double yy) {
		double m;
		double a;
		double e;
		double mm;

		a = 6378245;
		e = 0.00669342;

		mm = 1 - e * yj_sin2(x * 0.0174532925199433) * yj_sin2(x * 0.0174532925199433);
		m = (a * (1 - e)) / (mm * Math.sqrt(mm));

		return (yy * 180) / (m * 3.1415926);
	}

	private static double random_yj() {
		int t;
		int casm_a;
		int casm_c;
		casm_a = 314159269;
		casm_c = 453806245;
		casm_rr = casm_a * casm_rr + casm_c;
		t = (int) (casm_rr / 2);
		casm_rr = casm_rr - t * 2;
		casm_rr = casm_rr / 2;

		return (casm_rr);
	}

	private static void IniCasm(long w_time, long w_lng, long w_lat) {
		int tt;
		casm_t1 = w_time;
		casm_t2 = w_time;
		tt = (int) (w_time / 0.357);
		casm_rr = w_time - tt * 0.357;
		if (w_time == 0)
			casm_rr = 0.3;
		casm_x1 = w_lng;
		casm_y1 = w_lat;
		casm_x2 = w_lng;
		casm_y2 = w_lat;
		casm_f = 3;
	}

	/**
	 * 
	 * @param wg_flag
	 * @param wg_lng
	 * @param wg_lat
	 * @param wg_heit
	 * @param wg_week
	 * @param wg_time
	 * @return If the method succeeds, the return value is the point, otherwise
	 *         null
	 */
	
	
	
	private static GCJPoint wgtochina_lb(int wg_flag, int wg_lng, int wg_lat, int wg_heit, int wg_week, long wg_time) {
		double x_add;
		double y_add;
		double h_add;
		double x_l;
		double y_l;
		double casm_v;
		double t1_t2;
		double x1_x2;
		double y1_y2;

		GCJPoint point = null;

		if (wg_heit > 5000) {
			return point;
		}
		x_l = wg_lng;
		x_l = x_l / 3686400.0;
		y_l = wg_lat;
		y_l = y_l / 3686400.0;

		if (x_l < 72.004) {
			point = new GCJPoint();
			point.setLatitude(wg_lng);
			point.setLongitude(wg_lat);
			return point;
		}
		if (x_l > 137.8347) {
			point = new GCJPoint();
			point.setLatitude(wg_lng);
			point.setLongitude(wg_lat);
			return point;
		}
		if (y_l < 0.8293) {
			point = new GCJPoint();
			point.setLatitude(wg_lng);
			point.setLongitude(wg_lat);
			return point;
		}
		if (y_l > 55.8271) {
			point = new GCJPoint();
			point.setLatitude(wg_lng);
			point.setLongitude(wg_lat);
			return point;
		}
		if (wg_flag == 0) {
			IniCasm(wg_time, wg_lng, wg_lat);

			point = new GCJPoint();
			point.setLatitude(wg_lng);
			point.setLongitude(wg_lat);

			return point;
		}

		casm_t2 = wg_time;
		t1_t2 = (double) (casm_t2 - casm_t1) / 1000.0;
		if (t1_t2 <= 0) {
			casm_t1 = casm_t2;
			casm_f = casm_f + 1;
			casm_x1 = casm_x2;
			casm_f = casm_f + 1;
			casm_y1 = casm_y2;
			casm_f = casm_f + 1;
		} else {
			if (t1_t2 > 120) {
				if (casm_f == 3) {
					casm_f = 0;
					casm_x2 = wg_lng;
					casm_y2 = wg_lat;
					x1_x2 = casm_x2 - casm_x1;
					y1_y2 = casm_y2 - casm_y1;
					casm_v = Math.sqrt(x1_x2 * x1_x2 + y1_y2 * y1_y2) / t1_t2;
					if (casm_v > 3185) {
						return (point);
					}

				}
				casm_t1 = casm_t2;
				casm_f = casm_f + 1;
				casm_x1 = casm_x2;
				casm_f = casm_f + 1;
				casm_y1 = casm_y2;
				casm_f = casm_f + 1;
			}
		}
		x_add = Transform_yj5(x_l - 105, y_l - 35);
		y_add = Transform_yjy5(x_l - 105, y_l - 35);
		h_add = wg_heit;

		x_add = x_add + h_add * 0.001 + yj_sin2(wg_time * 0.0174532925199433) + random_yj();
		y_add = y_add + h_add * 0.001 + yj_sin2(wg_time * 0.0174532925199433) + random_yj();

		point = new GCJPoint();
		point.setLongitude((int) ((x_l + Transform_jy5(y_l, x_add)) * 3686400));
		point.setLatitude((int) ((y_l + Transform_jyj5(y_l, y_add)) * 3686400));

		return (point);
	}

	public static GCJPoint wgs2gcj(double lon, double lat) {
		return wgtochina_lb(1, (int) (lon * 3686400), (int) (lat * 3686400), 0, 0, 0);
	}

	private static String gcj2wgs(double lon, double lat) {
		GCJPoint temPoint = wgs2gcj(lon, lat);
		double d_lon = (double)temPoint.getLongitude() / 3686400  - lon;
		double d_lat = (double)temPoint.getLatitude() / 3686400  - lat;
		return (lon - d_lon) + "," + (lat - d_lat);
	}

	public static void main(String[] args) throws Exception {
		try {
			File dir = new File(Config.IN_DIR);
			File file[] = dir.listFiles();
			for(int i = 0 ;i<file.length;i++)
			{
				System.out.println(file[i].getName());
				BufferedReader br=new BufferedReader(new FileReader(file[i]));
				BufferedWriter bw=new BufferedWriter(new FileWriter(Config.OUT_DIR+"after_"+file[i].getName()));
				//BufferedWriter be=new BufferedWriter(new FileWriter("F:\\BJmobile\\20130226\\Temp_Logs\\error.log",true));
				String line="";
				long lineNumber = 0;
				
				while((line=br.readLine())!=null)
				{
					lineNumber++;
					if(lineNumber%1000000==0)
						System.out.println(file[i].getName()+","+lineNumber);
					String item[]=line.split(",");
					/*
					if(item.length!=7 || item[Config.LONTITUDE_ZIDUAN].length()<3 || item[Config.LATITUDE_ZIDUAN].length()<3)
					{
						//be.write(file[i].getName()+":"+lineNumber+"\n");
						continue;
					}
					*/
					double lng=0.0;
					double lat=0.0;
					lng=Double.parseDouble(item[Config.LONTITUDE_ZIDUAN]);
					lat=Double.parseDouble(item[Config.LATITUDE_ZIDUAN]);
					GCJPoint rs3 = TransGPS84toNormal.wgs2gcj(lng,lat);
					double nLng=rs3.getLongitude()/3686400.0;
					double nLat=rs3.getLatitude()/3686400.0;
					//System.out.println(nLng);
					//System.out.println(nLat);
					StringBuilder line_str=new StringBuilder("");
					for(int i1=0;i1<item.length-1;i1++){
						if(i1==Config.LONTITUDE_ZIDUAN){
							line_str.append(String.format("%.6f",nLng)).append(",");
						}
						else if (i1==Config.LATITUDE_ZIDUAN) {
							line_str.append(String.format("%.6f",nLat)).append(",");
						}
						else{
							line_str.append(item[i1]).append(",");
						}
					}
					line_str.append(item[item.length-1]).append("\r\n");
					bw.write(line_str.toString());
				}
				bw.close();
				br.close();
				//be.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
//		GCJPoint rs3 = TransGps.wgs2gcj(116.313453, 39.313453);
//		System.out.println(rs3.getLongitude()/ 3686400.0+","+rs3.getLatitude()/ 3686400.0);
	}
}
