
rcpmail06
- Folder uses IObservableList
- ObservableListContentProvider
- BeansObservables.observeMaps
- ObservableMapLabelProvider

rcpmail06MessageView
  (based on nobinding)
- add MessageTablelView
- add to perspective
- add to plugin.xml

rcpmail05
- Server and Model use IObservableCollections 

rcpmail05nobinding:
- use the model in the navigation view
- fix the ViewLabelProvider and the ViewContentProvider
- add a property change listener t0 the Model
- set input on property change
- add dispose listener to view to remove the listener

rcpmail04:
- added validation to the wizard
- WizardPageSupport.create(this, dbc);

rcpmail03:
- added basic data binding to the wizard

rcpmail02
- added a wizard for creating the server

rcpmail01
- added the model