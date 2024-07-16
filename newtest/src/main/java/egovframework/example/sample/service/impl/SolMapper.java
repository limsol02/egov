package egovframework.example.sample.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.example.sample.service.Competition;
import egovframework.example.sample.service.FileStorage;
import egovframework.example.sample.service.Participant;
@Mapper("solMapper")
public interface SolMapper {
	int insFile(FileStorage ins) throws Exception;
	int insPart(Participant ins) throws Exception;
	List<Competition> competitionList() throws Exception;
	List<Participant> partList(int competition_id) throws Exception;
}
