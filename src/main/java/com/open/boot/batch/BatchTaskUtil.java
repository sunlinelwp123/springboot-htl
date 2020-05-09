package com.open.boot.batch;

public class BatchTaskUtil {


    public BatchTaskUtil() {
    }
/*
    public static Map<String, String> initLogContext(DataArea dataArea, Integer bizLogLevel) {
        return initJobLogContext(dataArea, (String)null, bizLogLevel);
    }

    public static Map<String, String> initJobLogContext(DataArea dataArea, String jobName, Integer bizLogLevel) {
        Map<String, String> ret = LttsCoreBeanUtil.getLogManager().getContext();
        LttsCoreBeanUtil.getLogManager().clearContext();
        LogConfigManager.get().setCurrentLogType(LogType.busi_batch);
        LogConfigManager.get().setCurrentSystemType(SystemType.batch);
        if (StringUtil.isNotEmpty(bizLogLevel)) {
            LogConfigManager.get().setBizLogController(LogType.busi_batch, bizLogLevel.toString());
        }

        MapListDataContext systemDataArea = dataArea.getSystem();
        LttsCoreBeanUtil.getLogManager().putContext(systemDataArea.getMap());
        LogConfigManager.get().setCurrentSystemType(SystemType.batch);
        String flowId = (String)systemDataArea.get("pljylcbs");
        if (StringUtil.isNotEmpty(flowId)) {
            LttsCoreBeanUtil.getLogManager().putContext("pljyzbsh", (Object)null);
        }

        if (StringUtil.isNotEmpty(jobName)) {
            LttsCoreBeanUtil.getLogManager().putContext("jobname", jobName);
        }

        return ret;
    }*/
}
