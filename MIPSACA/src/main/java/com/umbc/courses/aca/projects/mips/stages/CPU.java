package com.umbc.courses.aca.projects.mips.stages;

public class CPU
{
    public static int CLOCK           = 0;

    public static int PROGRAM_COUNTER = 0;

    public static RUN RUN_TYPE        = RUN.MEMORY; // DEFAULT

    public enum RUN
    {
        PIPELINE, MEMORY
    };

}
