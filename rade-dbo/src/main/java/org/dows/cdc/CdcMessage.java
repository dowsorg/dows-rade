package org.dows.cdc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dows.core.message.Message;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CdcMessage implements Message, Serializable {
//    private Map<String, Object> data;
    private ChangeData data;
    private String dbType;
    private String handleType;
    private String database;
    private String table;
}
