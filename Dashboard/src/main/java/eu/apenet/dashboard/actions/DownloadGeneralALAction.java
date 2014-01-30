 package eu.apenet.dashboard.actions;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.AbstractAction;

 /**
 * User: Jara Alvarez
 * Date: Sep 20th, 2010
 *
 */

public class DownloadGeneralALAction extends AbstractAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7494922156718594589L;
	private final Logger log = Logger.getLogger(getClass());


	public String execute() {
        String result=null;
		return result;
	}

//     public String downloadArchivalLandscapeFromDB() {
//         try {
//             StringWriter eadArchilvaLandscapeWriter = new StringWriter();
//             CreateArchivalLandscapeEad createArchivalLandscapeEad = new CreateArchivalLandscapeEad(eadArchilvaLandscapeWriter);
//             createArchivalLandscapeEad.createEadContentData("Archives Portal Europe - Archival Landscape", "GENERAL_AL_EAD", null, null);
//
//             CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
//             List<Country> countries = countryDAO.findAll();
//             ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
//
//             for(Country country : countries) {
//                 createArchivalLandscapeEad.addInsideEad(country);
//                 List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsByCountryId(country.getId()); //Get main AIs (without parents)
//                 for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
//                     if(archivalInstitution.getParentAiId() == null) {
//                         createArchivalLandscapeEad.addInsideEad(archivalInstitution);
//                         recurenceLoop(createArchivalLandscapeEad, archivalInstitution);
//                         createArchivalLandscapeEad.writeEndElement(); //close each C element for each main archival institution
//                     }
//                 }
//                 createArchivalLandscapeEad.writeEndElement(); //close each C element for each country
//             }
//
//             createArchivalLandscapeEad.closeEndFile();
//             eadArchilvaLandscapeWriter.close();
//
//             setInputStream(IOUtils.toInputStream(eadArchilvaLandscapeWriter.toString()));
//             setFileName("AL.xml");
//
//             return "download";
//         } catch (Exception e) {
//             LOG.error("Error", e);
//             return ERROR;
//         }
//     }
//
//     public void recurenceLoop(CreateArchivalLandscapeEad createArchivalLandscapeEad, ArchivalInstitution archivalInstitution) throws XMLStreamException {
//         List<ArchivalInstitution> archivalInstitutionChildren = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionsByParentAiId(archivalInstitution.getAiId());
//         for(ArchivalInstitution archivalInstitutionChild : archivalInstitutionChildren) {
//             createArchivalLandscapeEad.addInsideEad(archivalInstitutionChild);
//             recurenceLoop(createArchivalLandscapeEad, archivalInstitutionChild);
//             createArchivalLandscapeEad.writeEndElement();
//         }
//     }

}