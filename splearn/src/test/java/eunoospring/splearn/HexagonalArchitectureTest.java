package eunoospring.splearn;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.DisplayName;

@AnalyzeClasses(packages = "eunoospring.splearn", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

    @ArchTest
    void haxagonalArchitecture(JavaClasses classes) {
        Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("domain").definedBy("eunoospring.splearn.domain..")
                .layer("application").definedBy("eunoospring.splearn.application..")
                .layer("adapter").definedBy("eunoospring.splearn.adapter..")
                .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
                .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
                .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
                .check(classes);
    }

    @ArchTest
    @DisplayName("domain 계층 내에서 순환 의존성이 존재하면 안된다.")
    void domainFreeOfCycle(JavaClasses classes) {
        SlicesRuleDefinition.slices()
                .matching("eunoospring.splearn.domain.(*)..")
                .should().beFreeOfCycles()
                .check(classes);
    }

    @ArchTest
    @DisplayName("application 계층 내에서 순환 의존성이 존재하면 안된다.")
    void applicationFreeOfCycle(JavaClasses classes) {
        SlicesRuleDefinition.slices()
                .matching("eunoospring.splearn.application.(*)..")
                .should().beFreeOfCycles()
                .check(classes);
    }
}
