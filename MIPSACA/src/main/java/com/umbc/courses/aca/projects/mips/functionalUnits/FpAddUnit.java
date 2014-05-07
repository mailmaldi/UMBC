package com.umbc.courses.aca.projects.mips.functionalUnits;

import com.umbc.courses.aca.projects.mips.config.ConfigManager;
import com.umbc.courses.aca.projects.mips.stages.StageType;

public class FpAddUnit extends FPFunctionalUnit
{

    private static volatile FpAddUnit instance;

    public static FpAddUnit getInstance()
    {
        if (null == instance)
            synchronized (FpAddUnit.class)
            {
                if (null == instance)
                    instance = new FpAddUnit();
            }

        return instance;
    }

    private FpAddUnit()
    {
        super();
        setPipelined(ConfigManager.instance.FPAdderPipelined);
        setClockCyclesRequired(ConfigManager.instance.FPAdderLatency);
        pipelineSize = isPipelined() ? ConfigManager.instance.FPAdderLatency : 1;
        stageId = StageType.EXSTAGE;
        createPipelineQueue(pipelineSize);
    }
}
