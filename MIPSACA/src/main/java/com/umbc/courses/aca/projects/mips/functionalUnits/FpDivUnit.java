package com.umbc.courses.aca.projects.mips.functionalUnits;

import com.umbc.courses.aca.projects.mips.config.ConfigManager;
import com.umbc.courses.aca.projects.mips.stages.StageType;

public class FpDivUnit extends FPFunctionalUnit
{

    private static volatile FpDivUnit instance;

    public static FpDivUnit getInstance()
    {
        if (null == instance)
            synchronized (FpDivUnit.class)
            {
                if (null == instance)
                    instance = new FpDivUnit();
            }

        return instance;
    }

    private FpDivUnit()
    {
        super();
        setPipelined(ConfigManager.instance.FPDividerPipelined);
        setClockCyclesRequired(ConfigManager.instance.FPDivideLatency);
        pipelineSize = isPipelined() ? ConfigManager.instance.FPDivideLatency : 1;
        stageId = StageType.EXSTAGE;
        createPipelineQueue(pipelineSize);
    }
}
