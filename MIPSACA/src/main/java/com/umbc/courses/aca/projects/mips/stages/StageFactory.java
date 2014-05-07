package com.umbc.courses.aca.projects.mips.stages;

public class StageFactory
{

    public static Stage getStage(StageType type)
    {
        Stage stage = null;
        switch (type)
        {
            case IFSTAGE:
                stage = FetchStage.getInstance();
                break;
            case IDSTAGE:
                stage = DecodeStage.getInstance();
                break;
            case EXSTAGE:
                stage = ExStage.getInstance();
                break;
            case WBSTAGE:
                stage = WriteBackStage.getInstance();
                break;
            default:
                break;
        }
        return stage;
    }

}
