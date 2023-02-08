package com.leesfamily.chuno.room.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RoomGameRelocationSlaveDocumentRequestDto {

    Long id;
    List<RoomGameStartSlaveDocumentDto> listSlaveDocument;
}
