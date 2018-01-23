
package apptemplate;

public class FrameRate {

    private long    basetime;   //測定基準時間
    private int     count;      //フレーム数
    private double   framerate;  //フレームレート

	private long error = 0;
	private int fps = 60;
	private long idealSleep = (1000 << 16) / fps;
	private long oldTime;
	private long newTime = System.currentTimeMillis() << 16;

    //コンストラクタ
    public FrameRate() {
        basetime = System.currentTimeMillis();  //基準時間をセット

    }

    //フレームレートを取得
    public double getFrameRate() {
        return framerate;
    }

    //描画時に呼ぶ
    public void update() {

        ++count;        //フレーム数をインクリメント
        long now = System.currentTimeMillis();      //現在時刻を取得
        if (now - basetime >= 1000)
        {       //１秒以上経過していれば
            framerate = (double)(count * 1000) / (double)(now - basetime);        //フレームレートを計算
            basetime = now;     //現在時刻を基準時間に
            count = 0;          //フレーム数をリセット
        }

		newTime = System.currentTimeMillis() << 16;
		long sleepTime = idealSleep - (newTime - oldTime) - error;
	    if (sleepTime < 0x20000) sleepTime = 0x20000;
		oldTime = newTime;
		try {
			Thread.sleep(sleepTime >> 16);
			newTime = System.currentTimeMillis() << 16;
			error = newTime - oldTime - sleepTime;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		oldTime = newTime;
    }
}