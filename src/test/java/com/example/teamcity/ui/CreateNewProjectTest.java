package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.request.unchecked.UncheckedProject;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static com.example.teamcity.api.errors.ErrorMessages.PROJECT_CREATION_WITHOUT_NAME;
import static com.example.teamcity.api.errors.ErrorMessages.PROJECT_CREATION_WITHOUT_URL;
public class CreateNewProjectTest extends BaseUiTest{
    @Test
    public void authorizedUserShouldBeAbleCreateNewProject() {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/Borohaa/Teamcity";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));
    }

    @Test
    public void authorizedUserShouldNotBeAbleCreateNewProjectWithoutURL() {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .clearURLInput()
                .createProjectWithoutUrl()
                .getUrlError().shouldHave(Condition.text(PROJECT_CREATION_WITHOUT_URL));

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .get(testData.getProject().getName())
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void authorizedUserShouldNotBeAbleCreateNewProjectWithoutProjectName() {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/Borohaa/Teamcity";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .clearNameInput()
                .setupProjectWithoutName(testData.getBuildType().getName())
                .getProjectNameErrorText().shouldHave(Condition.text(PROJECT_CREATION_WITHOUT_NAME));

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .get(testData.getProject().getName())
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }
}