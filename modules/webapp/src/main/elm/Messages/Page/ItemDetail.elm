{-
  Copyright 2020 Docspell Contributors

  SPDX-License-Identifier: GPL-3.0-or-later
-}

module Messages.Page.ItemDetail exposing
    ( Texts
    , de
    , gb
    )

import Messages.Comp.ItemDetail
import Messages.Comp.ItemDetail.EditForm


type alias Texts =
    { itemDetail : Messages.Comp.ItemDetail.Texts
    , editForm : Messages.Comp.ItemDetail.EditForm.Texts
    , editMetadata : String
    , collapseExpand : String
    }


gb : Texts
gb =
    { itemDetail = Messages.Comp.ItemDetail.gb
    , editForm = Messages.Comp.ItemDetail.EditForm.gb
    , editMetadata = "Edit Metadata"
    , collapseExpand = "Collapse/Expand"
    }


de : Texts
de =
    { itemDetail = Messages.Comp.ItemDetail.de
    , editForm = Messages.Comp.ItemDetail.EditForm.de
    , editMetadata = "Metadaten ändern"
    , collapseExpand = "Aus-/Einklappen"
    }
