module.exports = {
    root: true,
    env: {
        node: true
    },
    extends: ["plugin:vue/essential", "eslint:recommended", "@vue/prettier"],
    parserOptions: {
        parser: "babel-eslint"
    },
    rules: {
        "import/newline-after-import": 0,
        "no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
        "no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off",
        "max-len": [2, 120, 8],
        "block-scoped-var": 0, // treat var statements as if they were block scoped (off by default)
        complexity: 0, // specify the maximum cyclomatic complexity allowed in a program (off by default)
        "consistent-return": 0, // require return statements to either always or never specify values
        curly: 0, // specify curly brace conventions for all control statements
        "default-case": 0, // require default case in switch statements (off by default)
        "dot-notation": 0, // encourages use of dot notation whenever possible
        eqeqeq: 0, // require the use of === and !==
        "guard-for-in": 0, // make sure for-in loops have an if statement (off by default)
        "no-alert": 0, // disallow the use of alert, confirm, and prompt
        "no-caller": 0, // disallow use of arguments.caller or arguments.callee
        "no-div-regex": 0, // disallow division operators explicitly at beginning of regular expression (off by default)
        "no-else-return": 0, // disallow else after a return in an if (off by default)
        "no-empty-label": 0, // disallow use of labels for anything other then loops and switches
        "no-eq-null": 0, // disallow comparisons to null without a type-checking operator (off by default)
        "no-eval": 0, // disallow use of eval()
        "no-extend-native": 0, // disallow adding to native types
        "no-extra-bind": 0, // disallow unnecessary function binding
        "no-fallthrough": 0, // disallow fallthrough of case statements
        "no-floating-decimal": 0, // disallow the use of leading or trailing decimal points in numeric literals (off by default)
        "no-implied-eval": 0, // disallow use of eval()-like methods
        "no-iterator": 0, // disallow usage of __iterator__ property
        "no-labels": 0, // disallow use of labeled statements
        "no-lone-blocks": 0, // disallow unnecessary nested blocks
        "no-loop-func": 0, // disallow creation of functions within loops
        "no-multi-spaces": 0, // disallow use of multiple spaces
        "no-multi-str": 0, // disallow use of multiline strings
        "no-native-reassign": 0, // disallow reassignments of native objects
        "no-new": 0, // disallow use of new operator when not part of the assignment or comparison
        "no-new-func": 0, // disallow use of new operator for Function object
        "no-new-wrappers": 0, // disallows creating new instances of String, Number, and Boolean
        "no-octal": 0, // disallow use of octal literals
        "no-octal-escape": 0, // disallow use of octal escape sequences in string literals, such as var foo = "Copyright \251";
        "no-process-env": 0, // disallow use of process.env (off by default)
        "no-proto": 0, // disallow usage of __proto__ property
        "no-redeclare": 0, // disallow declaring the same variable more then once
        "no-return-assign": 0, // disallow use of assignment in return statement
        "no-script-url": 0, // disallow use of javascript: urls.
        "no-self-compare": 0, // disallow comparisons where both sides are exactly the same (off by default)
        "no-sequences": 0, // disallow use of comma operator
        "no-unused-expressions": 0, // disallow usage of expressions in statement position
        "no-void": 0, // disallow use of void operator (off by default)
        "no-warning-comments": 0, // disallow usage of configurable warning terms in comments, e.g. TODO or FIXME (off by default)
        "no-with": 0, // disallow use of the with statement
        "radix": 0, // require use of the second argument for parseInt() (off by default)
        "vars-on-top": 0, // requires to declare all vars on top of their containing scope (off by default)
        "wrap-iife": 0, // require immediate function invocation to be wrapped in parentheses (off by default)
        "yoda": 0, // require or disallow Yoda conditions
        "prefer-const": ["error", {
            "destructuring": "any",
            "ignoreReadBeforeAssign": false
        }]
    }
};