package org.zerock.club.service;


import org.zerock.club.dto.NoteDTO;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.Note;

import java.util.List;

public interface NoteService {

    Long register(NoteDTO noteDTO);

    NoteDTO get(Long num);

    void modify(NoteDTO noteDTO);

    void delete(Long num);

    List<NoteDTO> getAllWithWriter(String writerEmail);

    default Note dtoToEntity(NoteDTO noteDTO){
        return Note.builder()
                .num(noteDTO.getNum())
                .title(noteDTO.getTitle())
                .content(noteDTO.getTitle())
                .writer(ClubMember.builder().email(noteDTO.getWriterEmail()).build())
                .build();
    }

    default NoteDTO entityToDto(Note note){
        return NoteDTO.builder()
                .num(note.getNum())
                .title(note.getTitle())
                .content(note.getContent())
                .writerEmail(note.getWriter().getEmail())
                .regDate(note.getRegeDate())
                .modDate(note.getModDate())
                .build();
    }

}
