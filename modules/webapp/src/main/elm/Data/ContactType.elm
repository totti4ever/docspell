{-
  Copyright 2020 Docspell Contributors

  SPDX-License-Identifier: GPL-3.0-or-later
-}

module Data.ContactType exposing
    ( ContactType(..)
    , all
    , fromString
    , toString
    )


type ContactType
    = Phone
    | Mobile
    | Fax
    | Email
    | Docspell
    | Website


fromString : String -> Maybe ContactType
fromString str =
    case String.toLower str of
        "phone" ->
            Just Phone

        "mobile" ->
            Just Mobile

        "fax" ->
            Just Fax

        "email" ->
            Just Email

        "docspell" ->
            Just Docspell

        "website" ->
            Just Website

        _ ->
            Nothing


toString : ContactType -> String
toString ct =
    case ct of
        Phone ->
            "Phone"

        Mobile ->
            "Mobile"

        Fax ->
            "Fax"

        Email ->
            "Email"

        Docspell ->
            "Docspell"

        Website ->
            "Website"


all : List ContactType
all =
    [ Mobile
    , Phone
    , Email
    , Website
    , Fax
    , Docspell
    ]
