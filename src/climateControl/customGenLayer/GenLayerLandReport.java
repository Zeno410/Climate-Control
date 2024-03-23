
package climateControl.customGenLayer;

import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.Receiver;
import net.minecraft.world.gen.layer.GenLayer;

/**
 *
 * @author Zeno410
 */
public class GenLayerLandReport extends GenLayerPack {
    
    private boolean reported = false;
    private final Receiver<String> reportee;
    private final int distanceFromOrigin;
    
    public GenLayerLandReport (GenLayer parent,int distance,Receiver<String> reportee) {
        super(0L);
        this.parent = parent;
        this.distanceFromOrigin = distance;
        this.reportee = reportee;
    }

    @Override
    public int[] getInts(int var1, int var2, int var3, int var4) {
        if (!reported) {
            // send a report; then don't do it again
            report();
            reported = true;
        }
        return parent.getInts(var1, var2, var3, var4);
    }

    private void report() {
        int width = 2*distanceFromOrigin+1;
        int [] toReport  = parent.getInts(
                -distanceFromOrigin,
                -distanceFromOrigin,
                2*distanceFromOrigin+1,
                2*distanceFromOrigin+1);
        int total = 0;
        int[] climates = new int[5];
        climates[1]=0;
        climates[2]=0;
        climates[3]=0;
        climates[4]=0;
        for (int i = 0; i < 2*distanceFromOrigin+1;i++) {
            String report = "";
            for (int j = 0; j < 2*distanceFromOrigin+1;j++) {
                int value = toReport[i*(2*distanceFromOrigin+1)+j];
                if (value == 24) value =0;// deep ocean
                if (value == 0) {
                    report += ". ";
                } else {
                    if (value >0&&value<100000) {
                        int climate = value%4;
                        if (climate == 0) climate = 4;
                        climates[climate]+=1;
                        String result = ""+climate+" ";
                         report += result;// value +" ";
                    } else {
                         report +=  "1 ";
                    }
                    total++;
                }
            }
            reportee.accept(report);
        }
        reportee.accept("");
        reportee.accept("Land "+total +" of "+(width*width) + " "+
                (int)((float)(total*100)/((float)(width*width))+.5) + "%");
        if (climates[2]>0||climates[3]>0||climates[4]>0) {
            reportee.accept("Hot   " + climates[1] + " "+(int)((float)(climates[1]*100)/(float)total)+"%");
            reportee.accept("Warm  " + climates[2] + " "+(int)((float)(climates[2]*100)/(float)total)+"%");
            reportee.accept("Cool  " + climates[3] + " "+(int)((float)(climates[3]*100)/(float)total)+"%");
            reportee.accept("Snowy " + climates[4] + " "+(int)((float)(climates[4]*100)/(float)total)+"%");
        }
        reportee.accept("");
        for (int i = 0; i < 2*distanceFromOrigin+1;i++) {
            String report = "";
            for (int j = 0; j < 2*distanceFromOrigin+1;j++) {
                int value = i;//toReport[i*(2*distanceFromOrigin+1)+j];
                report += value+ " ";
            }
            //reportee.accept(report);
        }
        reportee.accept("");
        for (int i = 0; i < 2*distanceFromOrigin+1;i++) {
            String report = "";
            for (int j = 0; j < 2*distanceFromOrigin+1;j++) {
                int value = j;//toReport[i*(2*distanceFromOrigin+1)+j];
                report += value+ " ";
            }
            //reportee.accept(report);
        }
        reportee.done();
    }
}