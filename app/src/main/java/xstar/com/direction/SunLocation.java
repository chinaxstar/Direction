package xstar.com.direction;

/**
 * Created by xstar on 2016-12-02.
 */
public class SunLocation
{
	public static final String	TAG	= "SunLocation";
	// 平均日地距离 km
	public static final double	R0	= 1.49597890E8;

	/**
	 * θ=2πt／365.2422 t=N－N0 N为积日 在年内的序号
	 * N0=79.6764+0.2422×（年份－1985）－INT〔（年份－1985）／4
	 * 
	 * @param cita
	 * @return
	 */
	public double getER(double cita)
	{
		return 1.000423 + 0.032359 * Math.sin(cita) + 0.000086 * Math.sin(2 * cita) - 0.008349 * Math.cos(cita) + 0.000115 * Math.cos(2 * cita);
	}

	// 太阳赤纬角
	public double getED(double cita)
	{
		return 0.3723 + 23.2567 * Math.sin(cita) + 0.1149 * Math.sin(2 * cita) - 0.1712 * Math.sin(3 * cita) - 0.758 * Math.cos(cita) + 0.3656 * Math.cos(2 * cita) + 0.0201 * Math.cos(3 * cita);
	}
    // 太阳赤纬角
	public double getED(int dayOfYear)
	{
		double b = 2 * Math.PI * (284+dayOfYear)/365;
		return 23.45*Math.sin(b);
	}

	public double getET(double cita)
	{
		return 0.0028 - 1.9857 * Math.sin(cita) + 9.9059 * Math.sin(2 * cita) - 7.0924 * Math.cos(cita) - 0.6882 * Math.cos(2 * cita);
	}

	/**
	 * 计算日角 自转产生的角度 同一时刻下 同一经度 不同纬度的角度相同
	 */
	public double getSunAngle(double JD, double JF, int NF, int Y, int R, int S, int F)
	{
		float A = NF / 4f;//年份 计算闰年
		double K = 2 * Math.PI / 365.2422;//每小时的日角
		double N0 = 79.6764 + 0.2422 * (NF - 1985) - (int) ((NF - 1985) / 4f);//
		float B = A - (int) (A);
		float C = 32.8f;
		if (Y <= 2) C = 30.6f;//
		if (B == 0 && Y > 2) C = 31.8f;//闰年月份大于2
		int G = (int) (30.6 * Y - C + 0.5) + R;
		double L = (JD + JF / 60) / 15;//经度/15 从0经度转到当前经度的时间
		double H = S - 8 + F / 60d;//北京时间 -8 + 分钟/60
		double N = G + (H - L) / 24d;//积日
		double cita = (N - N0) * K;
		return cita;
	}

	/**
	 * 获取时角
	 *
	 * @param Sz
	 *            太阳真实时
	 * @param Fz
	 *            太阳真实分
	 * @return
	 */
	public double getSunHourAngle(double Sz, double Fz)
	{
		return (Sz + Fz / 60d - 12) * 15;
	}

	// 将北京时间转换成当地时间 每一经度相差约四分钟
	public double getSunBeiJingTrue(int S, int F, double JD, double cita)
	{
		double Sd = S + Math.abs((F - ((120 - JD)) * 4)) / 60;
		return Sd + getET(cita) / 60;
	}

	// 太阳高度（h⊙）的计算公式为
	// sinh⊙=sinδsinφ＋cosδcosφcosτ（8）
	// 式中，δ就是太阳赤纬角，即式（5）中的Ed，φ为当地的地理纬度，τ为当时的太阳时角。φ值不难获得，且一旦确定，不会改变。δ值的计算可以从前述程序中得到。唯一需要说明的是太阳时角的计算。其计算式为

    //太阳高度角随着地方时和太阳的赤纬的变化而变化。太阳赤纬（与太阳直射点纬度相等）以δ表示，观测地地理纬度用φ表示（太阳赤纬与地理纬度都是北纬为正，南纬为负），地方时(时角)以t表示，有太阳高度角的计算公式：
    //sin h=sin φ sin δ+cos φ cosδ cos t
	public double getSunHigh(double ED, double weidu, double sunhourangle)
	{
		double sin = Math.sin(ED) * Math.sin(weidu) + Math.cos(ED) * Math.cos(weidu) * Math.cos(sunhourangle);
		return Math.asin(sin) / 2 / Math.PI * 360;
	}

	public double getSunLocationAngle(double sunhight, double weidu, double ED)
	{
		double cosA = 0;
		if (sunhight == 0)
		{
			cosA = -Math.sin(ED) / Math.cos(weidu);
		}
		else
		{
			cosA = (Math.sin(sunhight) * Math.sin(weidu) - Math.sin(ED)) / (Math.cos(sunhight) * Math.cos(weidu));
		}
		if (cosA <= 0)
		{
			// 90~180
		}
		else
		{
			// 0~90

		}
		return Math.acos(cosA) / 2 / Math.PI * 360;
	}


}
