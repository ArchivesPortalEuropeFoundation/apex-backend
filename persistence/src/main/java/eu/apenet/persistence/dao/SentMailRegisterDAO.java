package eu.apenet.persistence.dao;

import java.util.Date;
import java.util.List;

import eu.apenet.persistence.exception.SentMailRegisterException;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.NormalUser;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.SentMailRegister;


/***
 * This class implements the specific methods and features in respect to SentMailRegister entitys.
 * @author paul
 *
 */

public interface SentMailRegisterDAO extends GenericDAO<SentMailRegister, Long> {
	public SentMailRegister getSentMailRegisterByPartner(User p) throws SentMailRegisterException;
	public SentMailRegister exitsSentMailRegisterOnDate (String validation_link, Date date) throws SentMailRegisterException;
	public SentMailRegister exitsValidationLinkSentMailRegister(String validation_link) throws SentMailRegisterException;
	public List<SentMailRegister> getSentMailRegisterByAi (Integer aiId);
	
}
