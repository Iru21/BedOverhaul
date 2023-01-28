package me.iru.bedoverhaul.util;

public interface ModifiedBedBlockEntity {
    Boolean getCanResetTime();
    void setCanResetTime(Boolean value);

    Boolean getCanSetSpawnPoint();
    void setCanSetSpawnPoint(Boolean value);
}
