
/**
 *
 * @author levian
 *  שיםלב: יש צורך ליצור מופע זה בכל פעם שצריך ,שעון
 *  כמו כן יש ליצור כל פעם מופע חדש של evTime
 *
 */
 
public class MyTimer72 extends Thread
{
    private final long time;
    private final Event64 evTime;

    public MyTimer72(long time,Event64 evTime)
    {
        this.time=time;
        this.evTime=evTime;
        setDaemon(true);
        start();
    }

    public void run()
    {
        try 
		{
            sleep(time);
        } catch (InterruptedException ex) {}
        evTime.sendEvent();
    }

}