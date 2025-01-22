/*
 * Copyright 2022-2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codelab.sdclibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import ca.uhn.fhir.context.FhirContext
import com.google.android.fhir.datacapture.QuestionnaireFragment
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Enumerations
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Person


class MainActivity : AppCompatActivity() {

  var questionnaireJsonString: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    questionnaireJsonString = getStringFromAssets("dataToQuestionnaire.json")

    // Initialize Patient
    val person = Person()
    person.addName().setFamily("Simpson").addGiven("James")

    // Convert patient to Map<String, String>
    val ctx = FhirContext.forR4()
    val parser = ctx.newJsonParser()
    val launchContextMap = mapOf("person" to parser.encodeResourceToString(person)) as Map<String, String>


    // Pass questionnaire anf patient to QuestionnaireFragment
    // serializing FHI Resources: https://hapifhir.io/hapi-fhir/docs/model/parsers.html
    val questionnaireFragment =
      QuestionnaireFragment
        .builder()
        .setQuestionnaireLaunchContextMap(launchContextMap)
        .setQuestionnaire(questionnaireJsonString!!)
        .build()

    // Step 3: Add the QuestionnaireFragment to the FragmentContainerView
    if (savedInstanceState == null) {
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        add(R.id.fragment_container_view, questionnaireFragment)
      }
    }

    // Submit button callback
    supportFragmentManager.setFragmentResultListener(
      QuestionnaireFragment.SUBMIT_REQUEST_KEY,
      this,
    ) { _, _ ->
      submitQuestionnaire()
    }
  }

  private fun submitQuestionnaire() =
    lifecycleScope.launch {
      // 5 Replace with code from the codelab to get a questionnaire response.

      // 6 Replace with code from the codelab to extract FHIR resources from QuestionnaireResponse.
    }

  private fun getStringFromAssets(fileName: String): String {
    return assets.open(fileName).bufferedReader().use { it.readText() }
  }
}
