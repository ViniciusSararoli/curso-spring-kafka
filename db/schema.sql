create database icomprasprodutos;

create table produto (
	idproduto serial not null primary key,
	nome varchar(200) null,
	valor decimal(16,2) not null
);
-- ***********************************************************
create database icompraspedidos;

create table pedido (
	idpedido serial not null primary key,
	idcliente bigint not null,
	dia timestamp not null default now(),
	idpagamento text,
	observacoes text,
	status int not null,
	total decimal(16,2) not null,
	codigo_rastreio varchar(300) null,
	url_nf text
);

create table item_pedido (
	iditem_pedido serial not null primary key,
	idpedido bigint  not null REFERENCES pedido (idpedido),
	idproduto bigint not null,
	quantidade int not null,
	valor decimal(16,2) not null
);
-- ***********************************************************
create database icomprasclientes;

create table cliente (
	idcliente serial not null primary key,
	nome varchar(200) null,
	cpf_cnpj BIGINT not null,
	telefone BIGINT null,
	email varchar(200) null
);

create table endereco (
	idendereco serial not null primary key,
	cep int not null,
	logradouro varchar(200) null,
	numero varchar(10) null,
	complemento varchar(100),
	cidade varchar(50) null,
	estado varchar(30) null,
	idcliente BIGINT REFERENCES cliente
);
