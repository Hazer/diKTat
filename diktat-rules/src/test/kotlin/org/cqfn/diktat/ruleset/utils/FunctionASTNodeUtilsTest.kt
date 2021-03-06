package org.cqfn.diktat.ruleset.utils

import com.pinterest.ktlint.core.ast.ElementType.FUN
import org.cqfn.diktat.util.applyToCode
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@Suppress("UnsafeCallOnNullableType")
class FunctionASTNodeUtilsTest {
    @Test
    fun `should detect parameters in function - no parameters`() {
        applyToCode("fun foo() { }", 1) { node, counter ->
            if (node.elementType == FUN) {
                Assertions.assertFalse(node.hasParameters())
                Assertions.assertTrue(node.parameterNames().isEmpty())
                counter.incrementAndGet()
            }
        }
    }

    @Test
    fun `should detect parameters in function`() {
        applyToCode("fun foo(a: Int) { }", 1) { node, counter ->
            if (node.elementType == FUN) {
                Assertions.assertTrue(node.hasParameters())
                Assertions.assertEquals(listOf("a"), node.parameterNames())
                counter.incrementAndGet()
            }
        }
    }
}
