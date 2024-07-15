package egovframework.example.sample.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.example.sample.service.FileStorage;
@Mapper("solMapper")
public interface SolMapper {
	int insFile(FileStorage ins) throws Exception;
}
