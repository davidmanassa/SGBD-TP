Begin Tran

	Insert Into Encomenda(EncId, ClienteId, Nome, Morada)
	Values (1, 10, 'Ant�nio', 'Aldeia do Monte')

	Insert Into EncLinha(EncId, ProdutoId, Designacao, Preco, Qtd)
	Values (1, 150, 'Batata', 200, 10)

	Insert Into EncLinha(EncId, ProdutoId, Designacao, Preco, Qtd)
	Values (1, 200, 'Feij�o', 300, 2)

Commit