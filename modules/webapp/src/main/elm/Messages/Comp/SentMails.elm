module Messages.Comp.SentMails exposing (Texts, gb)


type alias Texts =
    { from : String
    , date : String
    , recipients : String
    , subject : String
    , sent : String
    , sender : String
    }


gb : Texts
gb =
    { from = "From"
    , date = "Date"
    , recipients = "Recipients"
    , subject = "Subject"
    , sent = "Sent"
    , sender = "Sender"
    }