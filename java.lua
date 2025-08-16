return {
	"nvim-java/nvim-java",
	dependencies = {
		"neovim/nvim-lspconfig",
	},
	config = function()
		require("java").setup({
			spring_boot_tools = { enable = true },
			jdk = { auto_install = true },
		})
		require("lspconfig").jdtls.setup({})
	end,
}
