/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


public class Benchmark {
    public long startTime;
    public long stopTime;
    
    public String getTime(){
        long time = this.stopTime-this.startTime;
        long minutes = (time / 1000)  / 60;
        long seconds = (time / 1000) % 60;
        long milli = time - (seconds * 1000) - (minutes * 60000);
        return "time taken: " + minutes + " min " + seconds + " sec " + milli +" ms" ;
    }
    public void start(){
        this.startTime = System.currentTimeMillis();
    }
    public void end(){
        this.stopTime = System.currentTimeMillis();
    }
    
}
