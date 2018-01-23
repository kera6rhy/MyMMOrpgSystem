package apptemplate;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;



public class Recorder extends Thread
{
    private static final int BITS = 16;
    private static final int HZ = 8192;
    private static final int BUFFER_SIZE = 2048;
    private static final int MONO = 1;


    byte[] au_data =  new byte[BUFFER_SIZE];

    // リニアPCM 16bit 8000Hz x １秒
    private byte[] voice = new byte[ HZ * BITS / 8 * MONO ];
    private TargetDataLine target;
    //private AudioInputStream stream;
    SourceDataLine sourceDataline;



    // コンストラクタ
    public Recorder()
    {
        try
        {
            // オーディオフォーマットの指定
            AudioFormat linear = new AudioFormat(HZ,16,1,true,false);

            // ターゲットデータラインを取得
            DataLine.Info info = new DataLine.Info( TargetDataLine.class, linear );
            target = (TargetDataLine)AudioSystem.getLine( info );

            // ターゲットデータラインを開く
            target.open( linear );
             // 書き込み情報を生成
            DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class,linear);

            // 書き出しライン（ソースライン）を生成
            sourceDataline = (SourceDataLine)AudioSystem.getLine(sourceInfo);
            //System.out.println("source1="+sourceDataline);
            // ラインを開く
            sourceDataline.open(linear);


            // マイク入力開始
            target.start();
            sourceDataline.start();

            start();
            // 入力ストリームを取得
            //stream = new AudioInputStream( target );

        }
        catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void run(){
        long old=0,now;
        while(true){
            now = System.currentTimeMillis();
            sourceDataline.write(au_data, 0, target.read(au_data, 0, au_data.length));

            //System.out.println(""+(now-old) + "   data[0]="+au_data[0]);
            old = now;
            try{
                Thread.sleep(0);
            }catch(Exception e){

            }
        }
    }

    // データ取得
    public int [] getVoice()
    {
        int d[] = new int[BUFFER_SIZE/2];
        for(int i=0;i<BUFFER_SIZE/2;i++){
            d[i] = (au_data[i*2+0] + au_data[i*2+1]*256);
        }


        return d;
    }

    // 終了
    public void end()
    {
        // ターゲットデータラインを停止
        target.stop();

        // ターゲットデータラインを閉じる
        target.close();
    }
}