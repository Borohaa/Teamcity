package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.request.checked.CheckedBuildConfig;
import com.example.teamcity.api.request.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.admin.CreateNewBuildStep;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static com.example.teamcity.api.errors.ErrorMessages.CUSTOM_SCRIPT_ERROR;

public class CreateNewBuildConfigTest extends BaseUiTest{
    @Test
    public void authorizedUserShouldBeAbleCreateNewBuildConfig() {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/Borohaa/Teamcity";
        var customScript = "echo 'Hello World'";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        new CreateNewBuildStep()
                .navigateToBuildStepsSection()
                .selectAddBuildStep()
                .selectCommandLineBtn()
                .setupBuildStep(testData.getProject().getName(), customScript);

        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .get(testData.getBuildType().getName());
        softy.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
        softy.assertThat(buildConfig.getName()).isEqualTo(testData.getBuildType().getName());
        softy.assertThat(buildConfig.getProject().getName()).isEqualTo(testData.getBuildType().getProject().getName());
    }

    @Test
    public void authorizedUserShouldNotBeAbleCreateNewBuildConfigWithoutScript() {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/Borohaa/Teamcity";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        new CreateNewBuildStep()
                .navigateToBuildStepsSection()
                .selectAddBuildStep()
                .selectCommandLineBtn()
                .setupBuildStepWithoutScript(testData.getProject().getName())
                .getCustomScriptError().shouldHave(Condition.text(CUSTOM_SCRIPT_ERROR));

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .get(testData.getBuildType().getName()).then().statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
