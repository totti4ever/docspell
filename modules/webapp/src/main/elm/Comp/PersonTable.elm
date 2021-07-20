{-
  Copyright 2020 Docspell Contributors

  SPDX-License-Identifier: GPL-3.0-or-later
-}

module Comp.PersonTable exposing
    ( Model
    , Msg(..)
    , emptyModel
    , update
    , view2
    )

import Api.Model.Person exposing (Person)
import Comp.Basic as B
import Data.Flags exposing (Flags)
import Data.PersonUse
import Html exposing (..)
import Html.Attributes exposing (..)
import Messages.Comp.PersonTable exposing (Texts)
import Styles as S
import Util.Contact


type alias Model =
    { equips : List Person
    , selected : Maybe Person
    }


emptyModel : Model
emptyModel =
    { equips = []
    , selected = Nothing
    }


type Msg
    = SetPersons (List Person)
    | Select Person
    | Deselect


update : Flags -> Msg -> Model -> ( Model, Cmd Msg )
update _ msg model =
    case msg of
        SetPersons list ->
            ( { model | equips = list, selected = Nothing }, Cmd.none )

        Select equip ->
            ( { model | selected = Just equip }, Cmd.none )

        Deselect ->
            ( { model | selected = Nothing }, Cmd.none )



--- View2


view2 : Texts -> Model -> Html Msg
view2 texts model =
    table [ class S.tableMain ]
        [ thead []
            [ tr []
                [ th [ class "w-px whitespace-nowrap" ] []
                , th [ class "text-left pr-1 md:px-2" ]
                    [ text texts.use
                    ]
                , th [ class "text-left" ] [ text texts.basics.name ]
                , th [ class "text-left hidden sm:table-cell" ] [ text texts.basics.organization ]
                , th [ class "text-left hidden md:table-cell" ] [ text texts.contact ]
                ]
            ]
        , tbody []
            (List.map (renderPersonLine2 texts model) model.equips)
        ]


renderPersonLine2 : Texts -> Model -> Person -> Html Msg
renderPersonLine2 texts model person =
    tr
        [ classList [ ( "active", model.selected == Just person ) ]
        , class S.tableRow
        ]
        [ B.editLinkTableCell texts.basics.edit (Select person)
        , td [ class "text-left pr-1 md:px-2" ]
            [ div [ class "label inline-flex text-sm" ]
                [ Data.PersonUse.fromString person.use
                    |> Maybe.withDefault Data.PersonUse.Both
                    |> texts.personUseLabel
                    |> text
                ]
            ]
        , td []
            [ text person.name
            ]
        , td [ class "hidden sm:table-cell" ]
            [ Maybe.map .name person.organization
                |> Maybe.withDefault "-"
                |> text
            ]
        , td [ class "hidden md:table-cell" ]
            [ Util.Contact.toString person.contacts |> text
            ]
        ]
