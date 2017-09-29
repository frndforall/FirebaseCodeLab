package retrofit.cybersoft.testretrofit;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by nagendra on 07/04/16.
 */
public interface StackAPI {
    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    Call<StackQuestions> loadQuestions(@Query("tagged") String tags);
}
