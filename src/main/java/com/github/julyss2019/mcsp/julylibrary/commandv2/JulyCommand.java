package com.github.julyss2019.mcsp.julylibrary.commandv2;

/**
 * 局限性：
 * 1. 需要 主命令 - 子命令，至少需要两个arg。
 */
public interface JulyCommand {
    String getFirstArg();
    String getDescription();
}
