import js from "@eslint/js";
import globals from "globals";
import pluginReact from "eslint-plugin-react";

export default [
  { ignores: ["dist", "node_modules"] },
  js.configs.recommended,
  {
    files: ["**/*.{js,mjs,cjs,jsx}"],
    languageOptions: {
      globals: globals.browser,
      ecmaVersion: 2021,
      sourceType: "module",
      parserOptions: { ecmaFeatures: { jsx: true } }
    },
    settings: { react: { version: "18" } },
    rules: {
      "react/react-in-jsx-scope": "off",
      "react/prop-types": "warn"
    }
  },
  pluginReact.configs.flat.recommended,
  {
    files: ["**/*.test.{js,jsx}"],
    languageOptions: {
      globals: { ...globals.browser, ...globals.node, global: "readonly" }
    },
    rules: {
      "react/react-in-jsx-scope": "off",
      "react/prop-types": "off",
      "no-unused-vars": "off",
      "no-undef": "off"
    }
  }
];
