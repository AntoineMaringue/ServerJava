package API;

import java.util.Map;

public interface IParseur 
{

	Map<String, String>  getInformations(String requestHttpForGoogle);
        Map<String, String> getInformationsApiOpenFoodFacts(String json);

}
