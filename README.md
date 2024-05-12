# NOTEBOOK
## AVAILABLE COMMANDS
### Category commands
- [add-mcc-codes](#add-mcc-codes): adds new mccs
- [add-category](#add-category): adds a category and binds mcc codes to it (mcc codes must be already existing)
- [add-categories-to-category](#add-categories-to-category): adds categories or mcc codes to the category
- [add-category-to-categories](#add-category-to-categories): adds category or mcc code to the categories
- [show-categories](#show-categories): shows all categories
- [remove-category](#remove-category): removes category
- [show-categories-spending-in-month](#show-categories-spending-in-month): shows spending in categories in month
- [show-category-spending-by-month](#show-category-spending-by-month): shows spending in the category by month

### Transaction commands
- [add-transaction](#add-transaction): adds a transaction with the specified parameters
- [remove-transaction](#remove-transaction): removes a transaction(s) with the specified parameters


## add-mcc-codes
NAME: add-mcc-codes - adds new mccs

SYNOPSIS: add-mcc-codes [--mccs String[]] --help

OPTIONS

    --mccs String[]
    [Mandatory]

    --help or -h 
    help for add-mcc-codes
    [Optional]

### Sample
```
add-mcc-codes 5811,5812,5813,5814,5297,5298,7911,7912
created new mccs [5811, 5812, 5813, 5814, 5297, 5298, 7911, 7912]
```


## add-category
NAME: add-category - adds a category and binds mcc codes to it (mcc codes must be already existing)

SYNOPSIS: add-category [--name String] [--mccs String[]] --help

OPTIONS

    --name String
    [Mandatory]

    --mccs String[]
    [Mandatory]

    --help or -h 
    help for add-category
    [Optional]

### Sample
```
add-category рестораны 5811,5812,5813
created new category рестораны with mccs [5811, 5812, 5813]
```


## add-categories-to-category
NAME: add-categories-to-category - adds categories or mcc codes to the category

SYNOPSIS: add-categories-to-category [--name String] [--categories String[]] --help

OPTIONS

    --name String
    [Mandatory]

    --categories String[]
    [Mandatory]

    --help or -h 
    help for add-categories-to-category
    [Optional]

### Sample
```
add-categories-to-category еда рестораны,фасфуд,супермаркеты
added to category еда categories [рестораны, фасфуд, супермаркеты]
```


## add-category-to-categories
NAME: add-category-to-categories - adds category or mcc code to the categories

SYNOPSIS: add-category-to-categories [--name String] [--categories String[]] --help

OPTIONS

    --name String
    [Mandatory]

    --categories String[]
    [Mandatory]

    --help or -h 
    help for add-category-to-categories
    [Optional]

### Sample
```
add-category-to-categories рестораны развлечения,еда
added category рестораны to categories [развлечения, еда]
```


## show-categories
NAME: show-categories - shows all categories

SYNOPSIS: show-categories --help

OPTIONS

    --help or -h
    help for show-categories
    [Optional]

### Sample
```
show-categories
развлечения
рестораны
супермаркеты
фасфуд
```


## remove-category
NAME: remove-category - removes category

SYNOPSIS: remove-category [--name String] --help

OPTIONS

    --name String
    [Mandatory]

    --help or -h  
    help for remove-category 
    [Optional]

### Sample
```
remove-category еда
removed category еда
```


## show-categories-spending-in-month
NAME: show-categories-spending-in-month - shows spending in categories in month

SYNOPSIS: show-categories-spending-in-month [--month Month] --help

OPTIONS

    --month Month
    [Mandatory]

    --help or -h  
    help for show-categories-spending-in-month 
    [Optional]

### Sample
```
show-categories-spending-in-month March
фасфуд 0р 0%
супермаркеты 0р 0%
рестораны 318р 25%
еда 318р 25%
развлечения 1226р 100%
```


## show-category-spending-by-month
NAME: show-category-spending-by-month - shows spending in the category by month

SYNOPSIS: show-category-spending-by-month [--name String] --help

OPTIONS

    --name String
    [Mandatory]

    --help or -h 
    help for show-category-spending-by-month
    [Optional]

### Sample
```
show-category-spending-by-month развлечения
MARCH 1226р
MAY 556р
APRIL 1146р
```


## add-transaction
NAME: add-transaction - adds a transaction with the specified parameters

SYNOPSIS: add-transaction [--name String] [--value int] [--month Month] [--mcc String] --help

OPTIONS

    --name String
    [Mandatory]

    --value int
    [Mandatory]

    --month Month
    [Mandatory]

    --mcc String
    [Mandatory]

    --help or -h 
    help for add-transaction
    [Optional]

### Sample
```
add-transaction coffee 129 May 5811
added transaction coffee
```



## remove-transaction
NAME: remove-transaction - removes a transaction(s) with the specified parameters

SYNOPSIS: remove-transaction [--name String] [--value int] [--month Month] --help

OPTIONS

    --name String
    [Mandatory]

    --value int
    [Mandatory]

    --month Month
    [Mandatory]

    --help or -h 
    help for remove-transaction
    [Optional]

### Sample
```
remove-transaction coffee 129 May 5811
removed transaction(s) 1 coffee
```
