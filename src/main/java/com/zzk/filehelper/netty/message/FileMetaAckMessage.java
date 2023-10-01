package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zhouzekun
 * @Date 2023/10/1 9:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaAckMessage extends Message{

    private Integer id;

    private String filename;
    @Override
    public int getMessageType() {
        return MessageConfig.FILE_META_ACK_MESSAGE;
    }
}
