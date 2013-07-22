package eu.apenet.persistence.vo;

import javax.xml.bind.annotation.XmlType;



public enum FileType implements java.io.Serializable {

	ZIP("zip"), XML("xml"),XSL("xsl");
	private String name;

	private FileType(String name) {
		this.name = name;
	}
    public static FileType getType(String name){
        for(FileType type : FileType.values()){
            if(type.getName().equals(name))
                return type;
        }
        return null;
    }
	public String getName() {
		return name;
	}



}
