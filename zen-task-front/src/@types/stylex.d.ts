declare module '@stylexjs/postcss-plugin' {
  interface StyleXPostCSSOptions {
    include?: string[];
    useCSSLayers?: boolean;
  }
  
  function stylexPostcssPlugin(options?: StyleXPostCSSOptions): any;
  export = stylexPostcssPlugin;
}