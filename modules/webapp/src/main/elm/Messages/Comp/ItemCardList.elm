{-
  Copyright 2020 Docspell Contributors

  SPDX-License-Identifier: GPL-3.0-or-later
-}

module Messages.Comp.ItemCardList exposing
    ( Texts
    , de
    , gb
    )

import Messages.Comp.ItemCard


type alias Texts =
    { itemCard : Messages.Comp.ItemCard.Texts
    }


gb : Texts
gb =
    { itemCard = Messages.Comp.ItemCard.gb
    }


de : Texts
de =
    { itemCard = Messages.Comp.ItemCard.de
    }
