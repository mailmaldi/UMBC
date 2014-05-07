package com.umbc.courses.aca.projects.mips.main;

import com.umbc.courses.aca.projects.mips.results.ResultsManager;
import com.umbc.courses.aca.projects.mips.stages.Stage;
import com.umbc.courses.aca.projects.mips.stages.StageFactory;
import com.umbc.courses.aca.projects.mips.stages.StageType;

public class Pipeline
{
    private static volatile Pipeline instance;

    public static Pipeline getInstance()
    {

        if (null == instance)
            synchronized (Pipeline.class)
            {
                if (null == instance)
                    instance = new Pipeline();
            }

        return instance;
    }

    private Pipeline()
    {

    }

    public void execute()
    {
        Stage ifStage = StageFactory.getStage(StageType.IFSTAGE);
        Stage idStage = StageFactory.getStage(StageType.IDSTAGE);
        Stage exStage = StageFactory.getStage(StageType.EXSTAGE);
        Stage wbStage = StageFactory.getStage(StageType.WBSTAGE);

        try
        {
            // I run these many clock cycles after HLT to flush pipeline
            int extraCLKCount = 100;
            while (extraCLKCount != 0)
            {

                wbStage.execute();
                exStage.execute();

                // Well this is just stupid way of doing this
                // Halt is set only when Halt is decoded by decode stage
                if (!ResultsManager.instance.isHALT())
                {
                    idStage.execute();

                    if (!ResultsManager.instance.isHALT())
                    {
                        ifStage.execute();
                    }
                }
                else
                    extraCLKCount--;

                CPU.CLOCK++;
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR: CLOCK=" + CPU.CLOCK);
            e.printStackTrace();
        }
        finally
        {
        }
    }

}
