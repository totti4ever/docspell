{-
  Copyright 2020 Docspell Contributors

  SPDX-License-Identifier: GPL-3.0-or-later
-}

module Messages.Comp.TagTable exposing
    ( Texts
    , de
    , gb
    )

import Messages.Basics


type alias Texts =
    { basics : Messages.Basics.Texts
    , category : String
    }


gb : Texts
gb =
    { basics = Messages.Basics.gb
    , category = "Category"
    }


de : Texts
de =
    { basics = Messages.Basics.de
    , category = "Kategorie"
    }
