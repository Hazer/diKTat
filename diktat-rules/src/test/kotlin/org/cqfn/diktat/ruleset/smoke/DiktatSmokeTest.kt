package org.cqfn.diktat.ruleset.smoke

import com.pinterest.ktlint.core.LintError
import org.cqfn.diktat.ruleset.constants.Warnings.FILE_NAME_INCORRECT
import org.cqfn.diktat.ruleset.constants.Warnings.FILE_NAME_MATCH_CLASS
import org.cqfn.diktat.ruleset.constants.Warnings.HEADER_MISSING_IN_NON_SINGLE_CLASS_FILE
import org.cqfn.diktat.ruleset.constants.Warnings.KDOC_NO_EMPTY_TAGS
import org.cqfn.diktat.ruleset.constants.Warnings.MISSING_KDOC_CLASS_ELEMENTS
import org.cqfn.diktat.ruleset.constants.Warnings.MISSING_KDOC_TOP_LEVEL
import org.cqfn.diktat.ruleset.rules.DIKTAT_RULE_SET_ID
import org.cqfn.diktat.ruleset.rules.DiktatRuleSetProvider
import org.cqfn.diktat.util.FixTestBase
import org.cqfn.diktat.util.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.File

// fixme: run as a separate maven goal/module?
class DiktatSmokeTest : FixTestBase("test/smoke/src/main/kotlin",
        { DiktatRuleSetProvider() },
        { lintError, _ -> unfixedLintErrors.add(lintError) },
        null
) {
    companion object {
        private val unfixedLintErrors: MutableList<LintError> = mutableListOf()
    }

    @BeforeEach
    fun `prepare accumulator for LintErrors`() {
        unfixedLintErrors.clear()
    }

    @Test
    @Tag("DiktatRuleSetProvider")
    fun `smoke test #1`() {
        fixAndCompare("Example1Expected.kt", "Example1Test.kt")
        unfixedLintErrors.assertEquals(
                LintError(1, 1, "$DIKTAT_RULE_SET_ID:file-naming", "${FILE_NAME_INCORRECT.warnText()} Example1Test.kt_copy", true), // todo this is a false one
                LintError(1, 1, "$DIKTAT_RULE_SET_ID:file-naming", "${FILE_NAME_MATCH_CLASS.warnText()} Example1Test.kt_copy vs Example", true), // todo this is a false one
                LintError(1, 1, "$DIKTAT_RULE_SET_ID:kdoc-formatting", "${KDOC_NO_EMPTY_TAGS.warnText()} @return", false),
                LintError(4, 17, "$DIKTAT_RULE_SET_ID:kdoc-comments", "${MISSING_KDOC_TOP_LEVEL.warnText()} Example", false),
                LintError(6, 13, "$DIKTAT_RULE_SET_ID:kdoc-comments", "${MISSING_KDOC_CLASS_ELEMENTS.warnText()} isValid", false),
                LintError(10, 3, "$DIKTAT_RULE_SET_ID:kdoc-comments", "${MISSING_KDOC_CLASS_ELEMENTS.warnText()} foo", false), // todo what's with offset?
                LintError(11, 7, "$DIKTAT_RULE_SET_ID:kdoc-formatting", "${KDOC_NO_EMPTY_TAGS.warnText()} @return", false),
                LintError(15, 14, "$DIKTAT_RULE_SET_ID:kdoc-formatting", "${KDOC_NO_EMPTY_TAGS.warnText()} @return", false)
        )
    }

    @Test
    @Tag("DiktatRuleSetProvider")
    fun `smoke test #2`() {
        // fixme: path shouldn't point to `target` directory
        // val expectedFileAbsolutePath = Path.of(Path.of("target/test-classes/$resourceFilePath").toAbsolutePath().toString(), "Example2Test.kt_copy")

        val expectedFileAbsolutePath = File("target/test-classes/$resourceFilePath/Example2Test.kt_copy").absolutePath
        fixAndCompare("Example2Expected.kt", "Example2Test.kt")
        unfixedLintErrors.assertEquals(
                LintError(1, 1, "$DIKTAT_RULE_SET_ID:file-naming", "${FILE_NAME_INCORRECT.warnText()} Example2Test.kt_copy", true), // todo this is a false one
                LintError(1, 1, "$DIKTAT_RULE_SET_ID:header-comment", "${HEADER_MISSING_IN_NON_SINGLE_CLASS_FILE.warnText()} $expectedFileAbsolutePath", false)
        )
    }

    @Test
    @Tag("DiktatRuleSetProvider")
    fun `regression - shouldn't throw exception in cases similar to #371`() {
        fixAndCompare("Bug1Expected.kt", "Bug1Test.kt")
    }
}
