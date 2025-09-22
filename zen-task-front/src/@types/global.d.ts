/// <reference types="vite/client" />

// Declarações para arquivos CSS
declare module '*.css' {
  const content: Record<string, string>;
  export default content;
}

declare module '*.scss' {
  const content: Record<string, string>;
  export default content;
}

// Declaração para o plugin StyleX PostCSS
declare module '@stylexjs/postcss-plugin' {
  interface StyleXPostCSSOptions {
    include?: string[];
    useCSSLayers?: boolean;
    [key: string]: any;
  }
  
  function stylexPostcssPlugin(options?: StyleXPostCSSOptions): any;
  export default stylexPostcssPlugin;
  export = stylexPostcssPlugin;
}