package bmatser.service;

import java.util.List;

public interface AdverImageI {

	List findAdverImages(int positionId);

	List findAllAdverImages(int positionId) throws Exception;

	List findMallAdverImages(int positionId);

	List findAppMallAdverImages(int positionId);
}
