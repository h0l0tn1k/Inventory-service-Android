package com.sap.grc.riskframework;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.sap.grc.riskframework");

        noClasses()
            .that()
                .resideInAnyPackage("com.sap.grc.riskframework.service..")
            .or()
                .resideInAnyPackage("com.sap.grc.riskframework.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.sap.grc.riskframework.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
