package com.umbc.courses.aca.projects.mips.functionalUnits;

import com.umbc.courses.aca.projects.mips.config.ConfigManager;
import com.umbc.courses.aca.projects.mips.stages.StageType;

public class FpMulUnit extends FPFunctionalUnit
{

    private static volatile FpMulUnit instance;

    public static FpMulUnit getInstance()
    {
        if (null == instance)
            synchronized (FpMulUnit.class)
            {
                if (null == instance)
                    instance = new FpMulUnit();
            }

        return instance;
    }

    private FpMulUnit()
    {
        super();
        isPipelined = ConfigManager.instance.FPMultPipelined;
        clockCyclesRequired = ConfigManager.instance.FPMultLatency;
        pipelineSize = isPipelined ? ConfigManager.instance.FPMultLatency : 1;
        stageId = StageType.EXSTAGE;
        createPipelineQueue(pipelineSize);
    }
}
