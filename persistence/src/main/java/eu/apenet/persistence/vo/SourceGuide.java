package eu.apenet.persistence.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SourceGuide extends Ead {

    public SourceGuide(){}

    public SourceGuide(ArchivalInstitution archivalInstitution, FileState fileState, String title, Date uploadDate) {
		setFileState(fileState);
		setArchivalInstitution(archivalInstitution);
		setTitle(title);
		setUploadDate(uploadDate);
	}
}
