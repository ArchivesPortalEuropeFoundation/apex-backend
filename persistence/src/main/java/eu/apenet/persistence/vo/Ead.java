package eu.apenet.persistence.vo;

import java.util.Set;

public abstract class Ead  extends AbstractContent{
	/**
	 * 
	 */
	private static final long serialVersionUID = -831753351582189830L;


        //ToDo: make next 2 method private
	@Deprecated
	public abstract String getPathApenetead();
	@Deprecated
	public abstract void setPathApenetead(String pathApenetead);
	public abstract void setPath(String path);
	public abstract String getEadid();
	public abstract void setEadid(String eadid);
	public abstract Long getTotalNumberOfDaos();
	public abstract void setTotalNumberOfDaos(Long totalNumberOfDaos);
	public abstract Long getTotalNumberOfUnits() ;
	public abstract void setTotalNumberOfUnits(Long totalNumberOfUnits) ;
	public abstract Long getTotalNumberOfUnitsWithDao();
	public abstract void setTotalNumberOfUnitsWithDao(Long totalNumberOfUnitsWithDao);
    @Deprecated
	public abstract Set<EadContent> getEadContents() ;
	public abstract void setEadContents(Set<EadContent> eadContents) ;
	public abstract boolean isDynamic();
	public abstract void setDynamic(boolean dynamic);
	
    public EadContent getEadContent() {
        Set<EadContent> set = getEadContents();
        if(set == null || set.isEmpty())
            return null;
        return set.iterator().next();
    }


	@Override
	public String getPath() {
		return getPathApenetead();
	}
	@Override
	public String getIdentifier() {
		return getEadid();
	}
	@Override
	public void setIdentifier(String identifier) {
		setEadid(identifier);
	}
	@Override
	public String toString() {
		return this.getEadClass().getSimpleName() + " - (" + getEadid() + "," + getId() + ") ";
	}
	public Class<? extends Ead> getEadClass(){
		if (this instanceof FindingAid){
			return FindingAid.class;
		}else if (this instanceof HoldingsGuide){
			return HoldingsGuide.class;
		}else if (this instanceof SourceGuide){
			return SourceGuide.class;
		}else {
			return Ead.class;
		}
		
	}
    
}
